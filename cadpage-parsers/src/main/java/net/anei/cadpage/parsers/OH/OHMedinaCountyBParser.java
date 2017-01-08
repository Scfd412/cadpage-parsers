package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.dispatch.DispatchEmergitechParser;



public class OHMedinaCountyBParser extends DispatchEmergitechParser {
  
  public OHMedinaCountyBParser() {
    super(CITY_LIST, "MEDINA COUNTY", "OH", TrailAddrType.PLACE);
  }

  private static final String[] CITY_LIST = new String[]{
      
      "BRUNSWICK",
      "MEDINA",
      "RITTMAN",
      "WADSWORTH",
      
      "CHIPPEWA LAKE",
      "CRESTON",
      "GLORIA GLENS PARK",
      "LODI",
      "SEVILLE",
      "SPENCER",
      "WESTFIELD CENTER",
      
      "BRUNSWICK HILLS TWP",
      "CHATHAM TWP",
      "GRANGER TWP",
      "GUILFORD TWP",
      "HARRISVILLE TWP",
      "HINCKLEY TWP",
      "HOMER TWP",
      "LAFAYETTE TWP",
      "LITCHFIELD TWP",
      "LIVERPOOL TWP",
      "MEDINA TWP",
      "MONTVILLE TWP",
      "SHARON TWP",
      "SPENCER TWP",
      "WADSWORTH TWP",
      "WESTFIELD TWP",
      "YORK TWP",
      
      "BEEBETOWN",
      "HOMERVILLE",
      "LITCHFIELD",
      "VALLEY CITY",
  
  };
}
