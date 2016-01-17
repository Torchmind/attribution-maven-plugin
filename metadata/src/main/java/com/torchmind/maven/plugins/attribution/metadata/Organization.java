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
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Objects;

/**
 * Represents an organization to be attributed for contribution to a project.
 *
 * @author Johannes Donath
 */
@XmlRootElement(name = "organization")
public class Organization {
        @JsonProperty(value = "name", required = true)
        @XmlElement(name = "name", required = true)
        private final String name;
        @JsonProperty("url")
        @XmlElement(name = "url")
        private final String url;

        @SuppressWarnings("ConstantConditions")
        protected Organization() {
                this(null, null);
        }

        public Organization(@Nonnull String name, @Nullable String url) {
                this.name = name;
                this.url = url;
        }

        /**
         * Retrieves the organization name (in human readable form).
         *
         * @return the name.
         */
        @Nonnull
        public String getName() {
                return this.name;
        }

        /**
         * Retrieves the organization url.
         *
         * @return the url.
         */
        @Nullable
        public String getUrl() {
                return this.url;
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
                Organization that = (Organization) o;
                return Objects.equals(name, that.name) &&
                        Objects.equals(url, that.url);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int hashCode() {
                return Objects.hash(name, url);
        }
}
