
// authentication
export const REALM: string = "playerRealm";
export const CLIENT_ID: string = "player-Rest-api";
export const CLIENT_SECRET: string = "VnGUhkQrNAj1KQjRICmInaSK4oeSQFkV";
export const REQUEST_LOGIN: string = "/realms/" + REALM + "/protocol/openid-connect/token";
export const REQUEST_REG: string = "/" + REALM + "/protocol/openid-connect/token";
export const REQUEST_LOGOUT: string = "/realms/" + REALM + "/protocol/openid-connect/logout";
export const ADDRESS_AUTHENTICATION_SERVER: string = "http://localhost:8080";

