package io.identifiers.semantic;

import java.util.Objects;

import io.identifiers.Assert;

public final class Geo {

    private final double latitude;
    private final double longitude;


    public Geo(double latitude, double longitude) {
        Assert.state(latitude <= 90 && latitude >= -90, "latitude must be between -90.0 and 90.0, received %s", latitude);
        Assert.state(longitude <= 180 && longitude >= -180, "longitude must be between -180.0 and 180.0, received %s", longitude);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        Geo geo = (Geo) other;
        return Double.compare(geo.latitude, latitude) == 0
        && Double.compare(geo.longitude, longitude) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude);
    }

    @Override
    public String toString() {
        return String.format("lat:%s/long:%s", latitude, longitude);
    }
}
