/**
 * Copyright (c) 2017 Dell Inc., or its subsidiaries. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package io.pravega.segmentstore.contracts;

import io.pravega.common.Exceptions;
import io.pravega.common.util.ImmutableDate;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

/**
 * General Stream Segment Information.
 */
public class StreamSegmentInformation implements SegmentProperties {
    //region Members

    @Getter
    private final String name;
    @Getter
    private final long length;
    @Getter
    private final boolean sealed;
    @Getter
    private final boolean deleted;
    @Getter
    private final ImmutableDate lastModified;
    @Getter
    private final Map<UUID, Long> attributes;

    //endregion

    //region Constructor

    /**
     * Creates a new instance of the StreamSegmentInformation class.
     *
     * @param name         The name of the StreamSegment.
     * @param length       The length of the StreamSegment.
     * @param sealed       Whether the StreamSegment is sealed (for modifications).
     * @param deleted      Whether the StreamSegment is deleted (does not exist).
     * @param attributes   The attributes of this StreamSegment.
     * @param lastModified The last time the StreamSegment was modified.
     */
    @Builder
    private StreamSegmentInformation(String name, long length, boolean sealed, boolean deleted, Map<UUID, Long> attributes, ImmutableDate lastModified) {
        this.name = Exceptions.checkNotNullOrEmpty(name, "name");
        this.length = length;
        this.sealed = sealed;
        this.deleted = deleted;
        this.lastModified = lastModified == null ? new ImmutableDate() : lastModified;
        this.attributes = getAttributes(attributes);
    }

    /**
     * Creates a new instance of the StreamSegmentInformation class from a base SegmentProperties with replacement attributes.
     *
     * @param baseProperties The SegmentProperties to copy. Attributes will be ignored.
     * @param attributes     The attributes of this StreamSegment.
     */
    public StreamSegmentInformation(SegmentProperties baseProperties, Map<UUID, Long> attributes) {
        this.name = baseProperties.getName();
        this.length = baseProperties.getLength();
        this.sealed = baseProperties.isSealed();
        this.deleted = baseProperties.isDeleted();
        this.lastModified = baseProperties.getLastModified();
        this.attributes = getAttributes(attributes);
    }

    //endregion

    @Override
    public String toString() {
        return String.format("Name = %s, Length = %d, Sealed = %s, Deleted = %s, LastModified = %s", getName(), getLength(), isSealed(), isDeleted(), getLastModified());
    }

    private static Map<UUID, Long> getAttributes(Map<UUID, Long> input) {
        return input == null || input.size() == 0 ? Collections.emptyMap() : Collections.unmodifiableMap(input);
    }
}
