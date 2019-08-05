package battles.maps.parser;

import battles.maps.parser.map.Map;
import java.io.File;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class Parser {
   private Unmarshaller unmarshaller;

   public Parser() throws JAXBException {
      JAXBContext jc = JAXBContext.newInstance(Map.class);//Map.class);
      this.unmarshaller = jc.createUnmarshaller();
   }

   public Map parseMap(File file) throws JAXBException {
      return (Map)this.unmarshaller.unmarshal(file);
   }
}
