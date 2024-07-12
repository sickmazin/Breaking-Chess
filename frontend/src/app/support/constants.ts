
// authentication
export const REALM: string = "playerRealm";
export const CLIENT_ID: string = "player-Rest-api";
export const CLIENT_SECRET: string = "VnGUhkQrNAj1KQjRICmInaSK4oeSQFkV";
export const REQUEST_LOGIN: string = "/realms/" + REALM + "/protocol/openid-connect/token";
export const REQUEST_REG: string = "/" + REALM + "/protocol/openid-connect/token";
export const REQUEST_LOGOUT: string = "/realms/" + REALM + "/protocol/openid-connect/logout";
export const ADDRESS_AUTHENTICATION_SERVER: string = "http://localhost:8080";

//CONSTANT FOR YOUTUBE VIDEOS
export const YOUTUBE_KEY="AIzaSyBjPKQx2aNnpVeFE_vC-1Kpo_Nby4IFzy8"
export const channelID="UCQHX6ViZmPsWiYSFAyS0a3Q" //ID CANALE DI GOTHAM CHESS
export const PLAYLIST_ID="UUQHX6ViZmPsWiYSFAyS0a3Q"// ID PLAYLIST ULTIMI VIDEO DI GOTHAM
export const MAX_RESULTS_OF_VIDEOS=7
export const YOUTUBE_URL=`https://youtube.googleapis.com/youtube/v3/playlistItems?part=snippet&maxResults=${MAX_RESULTS_OF_VIDEOS}&playlistId=${PLAYLIST_ID}&key=${YOUTUBE_KEY}`
export const YOUTUBE_VIDEO_URL="https://www.youtube.com/watch?v="

//STATIC INFO FOR IMAGE
export const  MAN_AVATAR=[
    "../../assets/avatar/Man1.jpg",
    "../../assets/avatar/Man2.jpg",
    "../../assets/avatar/Man3.jpg",
    "../../assets/avatar/Man4.jpg",
    "../../assets/avatar/Man5.jpg",
    "../../assets/avatar/Man6.jpg",
    "../../assets/avatar/Man7.jpg",
    "../../assets/avatar/Man8.jpg",
    "../../assets/avatar/Man9.jpg",
    "../../assets/avatar/Man10.jpg",
    "../../assets/avatar/Man11.jpg",
    "../../assets/avatar/Man12.jpg",
    "../../assets/avatar/Man13.jpg",
    "../../assets/avatar/Man14.jpg",
    "../../assets/avatar/Man15.jpg",
]
export const WOMAN_AVATAR=[
    "assets/avatar/Women1.jpg",
    "assets/avatar/Women2.jpg",
    "assets/avatar/Women3.jpg",
    "assets/avatar/Women4.jpg",
    "assets/avatar/Women5.jpg",
    "assets/avatar/Women6.jpg",
    "assets/avatar/Women7.jpg",
    "assets/avatar/Women8.jpg",
    "assets/avatar/Women9.jpg",
    "assets/avatar/Women10.jpg",
    "assets/avatar/Women11.jpg",
    "assets/avatar/Women12.jpg",
    "assets/avatar/Women13.jpg",
    "assets/avatar/Women14.jpg",
    "assets/avatar/Women15.jpg",
]

//BACK END LINK
export const BASE_URL = "http://localhost:8081/api/";
export const HOME_URL = BASE_URL+"home/";
export const LEADERBOARD_URL= HOME_URL+"leaderboard"
export const GAMES_URL = BASE_URL+"games"
export const BOOK_URL=HOME_URL+"books"
export const LIKE_URL=BOOK_URL+"/like/"
// login e registrazione
export const ACCOUNT_URL= BASE_URL+"auth";
export const LOGIN_URL =ACCOUNT_URL+"/login"
export const CREATE_USER=ACCOUNT_URL+"/register";
