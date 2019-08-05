package collections.strings;

public class CaseInsensitiveString {
   private final String value;
   private final int hash;

   public String get() {
      return this.value;
   }

   public CaseInsensitiveString(String value) {
      this.value = value;
      String lc = value.toLowerCase();
      this.hash = lc.hashCode();
   }

   public boolean equals(Object o) {
      if(this == o) {
         return true;
      } else if(o instanceof CaseInsensitiveString) {
         CaseInsensitiveString that = (CaseInsensitiveString)o;
         return this.value.equalsIgnoreCase(that.value);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.hash;
   }
}
