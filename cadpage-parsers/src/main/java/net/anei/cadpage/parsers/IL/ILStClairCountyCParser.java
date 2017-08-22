package net.anei.cadpage.parsers.IL;

import net.anei.cadpage.parsers.dispatch.DispatchBCParser;

public class ILStClairCountyCParser extends DispatchBCParser {
  
  public ILStClairCountyCParser() {
    super("ST CLAIR COUNTY", "IL");
  }
  
  @Override
  public String getFilter() {
    return "ELLEVILLEPD@PUBLICSAFETYSOFTWARE.NET";
  }

}
