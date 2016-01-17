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
package com.torchmind.maven.plugins.attribution.metadata;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Provides a representation for artifacts.
 *
 * @author Johannes Donath
 */
@XmlRootElement(name = "artifact")
public class Artifact {
        @JsonProperty(value = "groupId", required = true)
        @XmlElement(name = "groupId", required = true)
        private final String groupId;
        @JsonProperty(value = "artifactId", required = true)
        @XmlElement(name = "artifactId", required = true)
        private final String artifactId;
        @JsonProperty(value = "version", required = true)
        @XmlElement(name = "version", required = true)
        private final String version;
        @JsonProperty("url")
        @XmlElement(name = "url")
        private final String url;
        @JsonProperty("name")
        @XmlElement(name = "name")
        private final String name;
        @JsonProperty("description")
        @XmlElement(name = "description")
        private final String description;
        @JsonProperty("licenses")
        @XmlElementWrapper(name = "licenses")
        @XmlElement(name = "license")
        private final List<License> licenses;
        @JsonProperty("developers")
        @XmlElementWrapper(name = "developers")
        @XmlElement(name = "developer")
        private final List<Developer> developers;
        @JsonProperty("contributors")
        @XmlElementWrapper(name = "contributors")
        @XmlElement(name = "contributor")
        private final List<Developer> contributors;

        @SuppressWarnings("ConstantConditions")
        protected Artifact() {
                this(null, null, null, null, null, null, null, null, null);
        }

        public Artifact(@Nonnull String groupId, @Nonnull String artifactId, @Nonnull String version, @Nullable String name, @Nullable String description, @Nullable String url, @Nullable List<License> licenses, @Nullable List<Developer> developers, @Nullable List<Developer> contributors) {
                this.groupId = groupId;
                this.artifactId = artifactId;
                this.version = version;

                this.name = name;
                this.description = description;
                this.url = url;
                this.licenses = (licenses != null ? Collections.unmodifiableList(licenses) : null);
                this.developers = (developers != null ? Collections.unmodifiableList(developers) : null);
                this.contributors = (contributors != null ? Collections.unmodifiableList(contributors) : null);
        }

        /**
         * Retrieves the artifact identifier.
         *
         * @return the identifier.
         */
        @Nonnull
        public String getArtifactId() {
                return this.artifactId;
        }

        /**
         * Retrieves a list of contributors associated with the artifact.
         *
         * @return the contributor list.
         */
        @Nullable
        public List<Developer> getContributors() {
                return this.contributors;
        }

        /**
         * Retrieves the artifact description.
         *
         * @return the description.
         */
        @Nullable
        public String getDescription() {
                return this.description;
        }

        /**
         * Retrieves a list of developers associated with the artifact.
         *
         * @return the developer list.
         */
        @Nullable
        public List<Developer> getDevelopers() {
                return this.developers;
        }

        /**
         * Retrieves the artifact group identifier.
         *
         * @return the identifier.
         */
        @Nonnull
        public String getGroupId() {
                return this.groupId;
        }

        /**
         * Retrieves a list of licenses associated with the artifact.
         *
         * @return the license list.
         */
        @Nullable
        public List<License> getLicenses() {
                return this.licenses;
        }

        /**
         * Retrieves the artifact name.
         *
         * @return the name.
         */
        @Nullable
        public String getName() {
                return this.name;
        }

        /**
         * Retrieves the artifact homepage.
         *
         * @return the url.
         */
        @Nullable
        public String getUrl() {
                return this.url;
        }

        /**
         * Retrieves the artifact version.
         *
         * @return the version.
         */
        @Nonnull
        public String getVersion() {
                return this.version;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean equals(Object o) {
                if (this == o) {
                        return true;
                }
                if (o == null || getClass() != o.getClass()) {
                        return false;
                }
                Artifact artifact = (Artifact) o;
                return Objects.equals(groupId, artifact.groupId) &&
                        Objects.equals(artifactId, artifact.artifactId) &&
                        Objects.equals(version, artifact.version);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int hashCode() {
                return Objects.hash(groupId, artifactId, version);
        }
}
