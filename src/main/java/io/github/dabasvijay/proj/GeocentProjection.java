package io.github.dabasvijay.proj;

import io.github.dabasvijay.ProjCoordinate;
import io.github.dabasvijay.datum.GeocentricConverter;

public class GeocentProjection extends Projection {

  @Override
  public ProjCoordinate projectRadians(ProjCoordinate src, ProjCoordinate dst) {
    GeocentricConverter geocentricConverter = new GeocentricConverter(this.ellipsoid);
    geocentricConverter.convertGeodeticToGeocentric(dst);
    return dst;
  }
  
  @Override
  public ProjCoordinate inverseProjectRadians(ProjCoordinate src, ProjCoordinate dst) {
    GeocentricConverter geocentricConverter = new GeocentricConverter(this.ellipsoid);
    geocentricConverter.convertGeocentricToGeodetic(dst);
    return dst;
  }
}
