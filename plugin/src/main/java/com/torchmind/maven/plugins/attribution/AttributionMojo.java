/*
 * Copyright 2016 Johannes Donath <johannesd@torchmind.com>
 * and other copyright owners as documented in the project's IP log.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.torchmind.maven.plugins.attribution;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.torchmind.maven.plugins.attribution.metadata.*;
import com.torchmind.maven.plugins.attribution.metadata.Developer;
import com.torchmind.maven.plugins.attribution.metadata.License;
import com.torchmind.maven.plugins.attribution.metadata.Organization;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.model.*;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.*;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectBuilder;
import org.apache.maven.project.ProjectBuildingException;
import org.apache.maven.repository.RepositorySystem;

import javax.annotation.Nonnull;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Provides a mojo for automatically generating a metadata file containing information on attributions to be made to
 * authors and organizations involved in creating one or more dependencies, plugins or sources within the application or
 * library the plugin is executed on.
 *
 * @author Johannes Donath
 */
@Mojo(name = "attribution", defaultPhase = LifecyclePhase.GENERATE_RESOURCES, requiresDependencyCollection = ResolutionScope.COMPILE_PLUS_RUNTIME)
public class AttributionMojo extends AbstractMojo {
        @Parameter(defaultValue = "${project}", readonly = true, required = true)
        private MavenProject project;

        @Component
        private RepositorySystem repositorySystem;
        @Component
        private MavenProjectBuilder mavenProjectBuilder;
        @Parameter(property = "project.remoteArtifactRepositories")
        protected List<ArtifactRepository> remoteRepositories;
        @Parameter(property = "localRepository")
        protected ArtifactRepository localRepository;

        @Parameter(name = "outputFile", defaultValue = "${project.build.outputDirectory}/attribution.json", required = true)
        private File outputFile;
        @Parameter(name = "outputType", defaultValue = "JSON", required = true)
        private OutputType outputType;
        @Parameter(name = "prettyPrint", defaultValue = "true", required = true)
        private boolean prettyPrint;

        /**
         * Creates an attribution object using a root artifact and its listed dependencies.
         * @param artifact the maven project.
         * @param dependencies the dependencies.
         * @return the attribution.
         */
        @Nonnull
        public static AttributionDocument createAttribution(@Nonnull MavenProject artifact, @Nonnull List<Artifact> dependencies, @Nonnull List<Artifact> plugins) {
                return new AttributionDocument(artifact.getGroupId(), artifact.getArtifactId(), artifact.getVersion(), artifact.getName(), artifact.getDescription(), artifact.getUrl(), artifact.getLicenses().stream().map(AttributionMojo::createLicense).collect(Collectors.toList()), artifact.getDevelopers().stream().map(AttributionMojo::createDeveloper).collect(Collectors.toList()), artifact.getContributors().stream().map(AttributionMojo::createDeveloper).collect(Collectors.toList()), dependencies, plugins);
        }

        /**
         * Creates an artifact using a maven project.
         * @param artifact the maven project.
         * @return the artifact.
         */
        @Nonnull
        public static Artifact createArtifact(@Nonnull MavenProject artifact) {
                return new Artifact(artifact.getGroupId(), artifact.getArtifactId(), artifact.getVersion(), artifact.getName(), artifact.getDescription(), artifact.getUrl(), artifact.getLicenses().stream().map(AttributionMojo::createLicense).collect(Collectors.toList()), artifact.getDevelopers().stream().map(AttributionMojo::createDeveloper).collect(Collectors.toList()), artifact.getContributors().stream().map(AttributionMojo::createDeveloper).collect(Collectors.toList()));
        }

        /**
         * Creates a license using a maven project.
         * @param license the license.
         * @return the license.
         */
        @Nonnull
        public static License createLicense(@Nonnull org.apache.maven.model.License license) {
                return new License(license.getName(), license.getUrl());
        }

        /**
         * Creates a developer using a contributor object.
         * @param contributor the contributor.
         * @return the developer.
         */
        @Nonnull
        public static Developer createDeveloper(@Nonnull Contributor contributor) {
                return new Developer(contributor.getName(), contributor.getEmail(), contributor.getUrl(), new Organization(contributor.getOrganization(), contributor.getOrganizationUrl()), contributor.getRoles());
        }

        /**
         * {@inheritDoc}
         */
        public void execute() throws MojoExecutionException, MojoFailureException {
                AttributionDocument document = createAttribution(
                        this.project,
                        this.project.getArtifacts().stream().map((a) -> {
                                try {
                                        return this.mavenProjectBuilder.buildFromRepository(a, this.remoteRepositories, this.localRepository);
                                } catch (ProjectBuildingException ex) {
                                        this.getLog().warn("Could not decode project metadata of artifact " + a.getGroupId() + ":" + a.getId(), ex);
                                        return null;
                                }
                        }).filter((a) -> a != null).map(AttributionMojo::createArtifact).collect(Collectors.toList()),
                        this.project.getPluginArtifacts().stream().map((a) -> {
                                try {
                                        return this.mavenProjectBuilder.buildFromRepository(a, this.remoteRepositories, this.localRepository);
                                } catch (ProjectBuildingException ex) {
                                        this.getLog().warn("Could not decode project metadata of artifact " + a.getGroupId() + ":" + a.getId(), ex);
                                        return null;
                                }
                        }).filter((a) -> a != null).map(AttributionMojo::createArtifact).collect(Collectors.toList())
                );

                this.outputFile.getParentFile().mkdirs();

                switch (this.outputType) {
                        case JSON:
                                ObjectMapper mapper = new ObjectMapper();

                                if (this.prettyPrint) {
                                        mapper.setDefaultPrettyPrinter(new DefaultPrettyPrinter());
                                }

                                try {
                                        mapper.writeValue(this.outputFile, document);
                                } catch (IOException ex) {
                                        throw new MojoFailureException("Could not write attribution document: " + ex.getMessage(), ex);
                                }
                                break;
                        case XML:
                                try {
                                        JAXBContext ctx = JAXBContext.newInstance(AttributionDocument.class);
                                        Marshaller marshaller = ctx.createMarshaller();

                                        marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "https://www.torchmind.com/schema/2016/attribution.xsd");
                                        if (prettyPrint) {
                                                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                                        }

                                        marshaller.marshal(document, this.outputFile);
                                } catch (JAXBException ex) {
                                        throw new MojoFailureException("Could not write attribution document: " + ex.getMessage(), ex);
                                }
                                break;
                }
        }

        /**
         * Provides a list of valid file output types.
         */
        public enum OutputType {
                /**
                 * Serializes the attribution file using the JavaScript Object Notation.
                 */
                JSON,

                /**
                 * Serializes the attribution file using the eXtensible Markup Language.
                 */
                XML
        }
}
