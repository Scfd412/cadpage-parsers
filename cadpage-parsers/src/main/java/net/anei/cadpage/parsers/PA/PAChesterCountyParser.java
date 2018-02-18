package net.anei.cadpage.parsers.PA;
import net.anei.cadpage.parsers.GroupBestParser;
import net.anei.cadpage.parsers.GroupBlockParser;

/* 
 * Chester Country, PA
 */


public class PAChesterCountyParser extends GroupBestParser {
  
  public PAChesterCountyParser() {
    super(new PAChesterCountyGParser(),  // PAChesterCountyG is the gold standard
          new GroupBlockParser(),        // If it works, don't bother with anything else  
          
          new PAChesterCountyAParser(), 
          new PAChesterCountyBParser(), 
          new PAChesterCountyCParser(), 
          new PAChesterCountyD1Parser(),
          new PAChesterCountyD2Parser(), 
          new PAChesterCountyD3Parser(), 
          new PAChesterCountyD4Parser(), 
          new PAChesterCountyD5Parser(), 
          new PAChesterCountyEParser(),
          new PAChesterCountyFParser(),
          new PAChesterCountyHParser(),
          new PAChesterCountyIParser(),
          new PAChesterCountyJParser(),
          new PAChesterCountyLParser(),
          new PAChesterCountyMParser(),
          new PAChesterCountyNParser(),
          new PAChesterCountyOParser(),
          new PAChesterCountyPParser());
  }

  @Override
  public String getLocName() {
    return "Chester County, PA";
  }
} 
