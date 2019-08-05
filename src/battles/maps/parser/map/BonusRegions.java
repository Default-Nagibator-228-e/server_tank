//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package battles.maps.parser.map;

import battles.maps.parser.map.bonus.BonusRegion;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(
        name = "bonus-regions"
)
class BonusRegions {
   private List<BonusRegion> bonusRegions;

   BonusRegions() {
   }

   public List<BonusRegion> getBonusRegions() {
      return this.bonusRegions;
   }

   @XmlElement(
           name = "bonus-region"
   )
   public void setBonusRegions(List<BonusRegion> bonusRegions) {
      this.bonusRegions = bonusRegions;
   }
}
