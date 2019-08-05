package bugs;

import utils.StringUtils;

public class BugInfo {
   private String description;
   private String summary;

   public BugInfo(String descrition, String summary) {
      this.description = descrition;
      this.summary = summary;
   }

   public String getDescription() {
      return this.description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public String getSummary() {
      return this.summary;
   }

   public void setSummary(String summary) {
      this.summary = summary;
   }

   public String toString() {
      return StringUtils.concatStrings(new String[]{"  Summary: ", this.summary, "\n", "  Description: ", this.description, "\n"});
   }
}
