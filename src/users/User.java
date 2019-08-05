package users;

import utils.StringUtils;
import groups.UserGroup;
import lobby.LobbyManager;
import network.Session;
import system.localization.Localization;
import users.anticheat.AntiCheatData;
import users.friends.Friends;
import users.garage.Garage;
import users.karma.Karma;
import users.locations.UserLocation;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@org.hibernate.annotations.Entity
@Table(
   name = "users"
)
public class User implements Serializable {
   private static final long serialVersionUID = 1594026136266908606L;
   @Id
   @GeneratedValue(
      strategy = GenerationType.IDENTITY
   )
   @Column(
      name = "uid",
      unique = true,
      nullable = false
   )
   private long id;
   @Column(
      name = "nickname",
      unique = true,
      nullable = false
   )
   private String nickname;
   @Column(
      name = "password",
      unique = true,
      nullable = false
   )
   private String password;
   @Column(
      name = "rank",
      unique = true,
      nullable = false
   )
   private int rang = 0;
   @Column(
      name = "score",
      unique = true,
      nullable = false
   )
   private int score = 0;
   @Column(
      name = "crystalls",
      unique = true,
      nullable = false
   )
   private int crystall = 0;
   @Column(
      name = "next_score",
      unique = true,
      nullable = false
   )
   private int nextScore = 100;
   @Column(
      name = "place",
      unique = true,
      nullable = false
   )
   private int place = 0;
   @Column(
      name = "rating",
      unique = true,
      nullable = false
   )
   private int rating = 0;
   @Column(
      name = "email",
      unique = true,
      nullable = true
   )
   private String email = "default@gtanks.com";
   @Column(
      name = "last_ip",
      unique = false,
      nullable = false
   )
   private String lastIP = "127.0.0.1";
   @Column(
      name = "user_type",
      unique = true,
      nullable = false
   )
   @Enumerated(EnumType.ORDINAL)
   private TypeUser type;
   @Column(
      name = "last_issue_bonus",
      nullable = true
   )
   private Date lastIssueBonus;
   @Transient
   private UserLocation userLocation;
   @Transient
   private Karma karma;
   @Transient
   private Garage garage;
   @Transient
   private Friends friend;
   @Transient
   private int warnings;
   @Transient
   private AntiCheatData antiCheatData;
   @Transient
   public Session session;
   @Transient
   private UserGroup userGroup;
   @Transient
   private Localization localization;
   @Transient
   private LobbyManager lobbyManager;
   public String idbat = "";
   public String opro = "";
   public int y = 0;//убил
   public int p = 0;//подбит
   public int yp = 0;//у/п
   public int zv = 0;
   public int part = 0;
   public boolean bp = false;
   public String invite = "";

   public User(String nickname, String password) {
      this.type = TypeUser.DEFAULT;
      this.antiCheatData = new AntiCheatData();
      this.nickname = nickname;
      this.password = password;
      this.garage = new Garage();
      this.idbat = "";
      //Main.opdf2.add(this);
   }

   public User() {
      this.type = TypeUser.DEFAULT;
      this.antiCheatData = new AntiCheatData();
      this.idbat = "";
      //Main.opdf2.add(this);
   }

   public String getNickname() {
      return this.nickname;
   }

   public void setNickname(String nickname) {
      this.nickname = nickname;
   }

   public String getPassword() {
      return this.password;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public int getRang() {
      return this.rang;
   }

   public void setRang(int rang) {
      this.rang = rang;
   }

   public int getScore() {
      return this.score;
   }

   public void setScore(int score) {
      this.score = score;
   }

   public void addScore(int score) {
      this.score += score;
   }

   public int getCrystall() {
      return this.crystall;
   }

   public void setCrystall(int crystall) {
      this.crystall = crystall;
   }

   public void addCrystall(int crystall) {
      this.crystall += crystall;
   }

   public int getNextScore() {
      return this.nextScore;
   }

   public void setNextScore(int nextScore) {
      this.nextScore = nextScore;
   }

   public int getPlace() {
      return this.place;
   }

   public void setPlace(int place) {
      this.place = place;
   }

   public int getRating() {
      return this.rating;
   }

   public void setRating(int rating) {
      this.rating = rating;
   }

   public Friends getFriend() {
      return friend;
   }

   public void setFriend(Friends r) {
      friend = r;
   }

   public Garage getGarage() {
      return this.garage;
   }

   public void setGarage(Garage garage) {
      this.garage = garage;
   }

   public LobbyManager getLobby() {
      return lobbyManager;
   }

   public void setLobby(LobbyManager lob) {
      lobbyManager = lob;
   }

   public String getEmail() {
      return this.email;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public TypeUser getType() {
      return this.type;
   }

   public void setType(TypeUser type) {
      this.type = type;
   }

   public UserLocation getUserLocation() {
      return this.userLocation;
   }

   public void setUserLocation(UserLocation userLocation) {
      this.userLocation = userLocation;
   }

   public Karma getKarma() {
      return this.karma;
   }

   public void setKarma(Karma karma) {
      this.karma = karma;
   }

   public int getWarnings() {
      return this.warnings;
   }

   public void setWarnings(int warnings) {
      this.warnings = warnings;
   }

   public void addWarning() {
      ++this.warnings;
   }

   public AntiCheatData getAntiCheatData() {
      return this.antiCheatData;
   }

   public void setAntiCheatData(AntiCheatData antiCheatData) {
      this.antiCheatData = antiCheatData;
   }

   public long getId() {
      return this.id;
   }

   public void setId(long id) {
      this.id = id;
   }

   public String getLastIP() {
      return this.lastIP;
   }

   public void setLastIP(String lastIP) {
      this.lastIP = lastIP;
   }

   public UserGroup getUserGroup() {
      return this.userGroup;
   }

   public void setUserGroup(UserGroup userGroup) {
      this.userGroup = userGroup;
   }

   public Date getLastIssueBonus() {
      return this.lastIssueBonus;
   }

   public void setLastIssueBonus(Date lastIssueBonus) {
      this.lastIssueBonus = lastIssueBonus;
   }

   public String toString() {
      return StringUtils.concatStrings(new String[]{String.valueOf(this.rang), " ", this.nickname, " ", this.password});
   }

   public Localization getLocalization() {
      return this.localization;
   }

   public void setLocalization(Localization localization) {
      this.localization = localization;
   }
}
