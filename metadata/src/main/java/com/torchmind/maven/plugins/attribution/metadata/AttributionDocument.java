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
 * Represents the root attribution document and all metadata encoded therein.
 *
 * @author Johannes Donath
 */
@XmlRootElement(name = "attribution")
public class AttributionDocument extends Artifact {
        @JsonProperty("dependencies")
        @XmlElementWrapper(name = "dependencies")
        @XmlElement(name = "dependency")
        private final List<Artifact> dependencies;
        @JsonProperty("plugins")
        @XmlElementWrapper(name = "plugins")
        @XmlElement(name = "plugin")
        private final List<Artifact> plugins;

        @SuppressWarnings("ConstantConditions")
        protected AttributionDocument() {
                this(null, null, null, null, null, null, null, null, null, null, null);
        }

        public AttributionDocument(@Nonnull String groupId, @Nonnull String artifactId, @Nonnull String version, @Nullable String name, @Nullable String description, @Nullable String url, @Nullable List<License> licenses, @Nullable List<Developer> developers, @Nullable List<Developer> contributors, @Nullable List<Artifact> dependencies, @Nullable List<Artifact> plugins) {
                super(groupId, artifactId, version, name, description, url, licenses, developers, contributors);
                this.dependencies = (dependencies != null ? Collections.unmodifiableList(dependencies) : null);
                this.plugins = (plugins != null ? Collections.unmodifiableList(plugins) : null);
        }

        /**
         * Retrieves a list of dependencies directly associated with the artifact.
         *
         * @return the list.
         */
        @Nullable
        public List<Artifact> getDependencies() {
                return this.dependencies;
        }

        /**
         * Retrieves a list of plugins directly associated with the artifact.
         * @return the list.
         */
        @Nullable
        public List<Artifact> getPlugins() {
                return this.plugins;
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
                if (!super.equals(o)) {
                        return false;
                }
                AttributionDocument that = (AttributionDocument) o;
                return Objects.equals(dependencies, that.dependencies);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int hashCode() {
                return Objects.hash(super.hashCode(), dependencies);
        }
}
