export type UserInfo = {
    uuid: string,
    name: string,
    email: string,
    pic: string,
    stats: UserStats,
}

export type UserStats = {
    highestLPM: number,
    highestAccuracy: number,
    totalRaces: number,
    avgLPM: number,
    avgAccuracy: number
}