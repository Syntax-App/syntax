export type UserInfo = {
    uuid: string,
    name: string,
    email: string,
    pic: string,
    stats: UserStats,
}

export type UserStats = {
    highlpm: number,
    highacc: number,
    numraces: number,
    avglpm: number,
    avgacc: number
}