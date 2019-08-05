package users.karma;

public class UserKarmaAction {
   private UserKarmaActionType actionType;
   private String comment;

   public UserKarmaAction(UserKarmaActionType actionType, String comment) {
      this.actionType = actionType;
      this.comment = comment;
   }

   public UserKarmaAction(UserKarmaActionType actionType) {
      this.actionType = actionType;
      this.comment = "NULL";
   }

   public UserKarmaActionType getActionType() {
      return this.actionType;
   }

   public String getComment() {
      return this.comment;
   }
}
