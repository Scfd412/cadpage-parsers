package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.dispatch.DispatchGeoconxParser;

/**
 * Marshall County, AL
 */
public class INNobleCountyParser extends DispatchGeoconxParser {


  public INNobleCountyParser() {
    super("NOBLE COUNTY", "IN");
  }
  
  @Override
  public String getFilter() {
    return "combs155@hotmail.com,dispatch@911email.org,kendalsvillepdin@911email.net";
  }
}
