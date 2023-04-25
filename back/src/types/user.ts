export type UserInfo = {
  uuid: string;
  name: string;
  email: string;
  pic: string;
  stats: UserStats;
};

export type UserStats = {
  highlpm: number;
  highacc: number;
  numraces: number;
  avglpm: number;
  avgacc: number;
};

export function isUserInfo(obj: Object): obj is UserInfo {
  if (!("uuid" in obj)) return false;
  if (!("name" in obj)) return false;
  if (!("email" in obj)) return false;
  if (!("pic" in obj)) return false;
  if (!("stats" in obj)) return false;
  return true;
}
