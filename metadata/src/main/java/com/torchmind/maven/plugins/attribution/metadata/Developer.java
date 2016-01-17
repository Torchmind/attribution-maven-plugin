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
 * Represents a developer that is to be attributed for contributing to a project.
 *
 * @author Johannes Donath
 */
@XmlRootElement(name = "developer")
public class Developer {
        @JsonProperty(value = "name", required = true)
        @XmlElement(name = "name", required = true)
        private final String name;
        @JsonProperty("email")
        @XmlElement(name = "email")
        private final String email;
        @JsonProperty("url")
        @XmlElement(name = "url")
        private final String url;
        @JsonProperty("organization")
        @XmlElement(name = "organization")
        private final Organization organization;
        @JsonProperty("roles")
        @XmlElementWrapper(name = "roles")
        @XmlElement(name = "role")
        private final List<String> roles;

        @SuppressWarnings("ConstantConditions")
        protected Developer() {
                this(null, null, null, null, null);
        }

        public Developer(@Nonnull String name, @Nullable String email, @Nullable String url, @Nullable Organization organization, @Nullable List<String> roles) {
                this.name = name;
                this.email = email;
                this.url = url;
                this.organization = organization;
                this.roles = (roles != null ? Collections.unmodifiableList(roles) : null);
        }

        /**
         * Retrieves the developer email address (if present).
         *
         * @return the email address.
         */
        @Nullable
        public String getEmail() {
                return this.email;
        }

        /**
         * Retrieves the developer name.
         *
         * @return the name.
         */
        @Nonnull
        public String getName() {
                return this.name;
        }

        /**
         * Retrieves the associated organization (if any).
         *
         * @return the organization.
         */
        @Nullable
        public Organization getOrganization() {
                return this.organization;
        }

        /**
         * Retrieves the developer roles (if any).
         *
         * @return the roles.
         */
        @Nullable
        public List<String> getRoles() {
                return this.roles;
        }

        /**
         * Retrieves the developer homepage url (if present).
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
                Developer developer = (Developer) o;
                return Objects.equals(name, developer.name) &&
                        Objects.equals(email, developer.email) &&
                        Objects.equals(url, developer.url) &&
                        Objects.equals(organization, developer.organization);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int hashCode() {
                return Objects.hash(name, email, url, organization);
        }
}
