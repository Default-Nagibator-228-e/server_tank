package users;

public enum TypeUser {
   DEFAULT {
      public String toString() {
         return "default";
      }
   },
   MODERATOR {
      public String toString() {
         return "moderator";
      }
   },
   ADMIN {
      public String toString() {
         return "admin";
      }
   },
   TESTER {
      public String toString() {
         return "tester";
      }
   };

   private TypeUser() {
   }

   // $FF: synthetic method
   TypeUser(TypeUser var3) {
      this();
   }
}
