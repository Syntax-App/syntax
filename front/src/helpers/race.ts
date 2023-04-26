import { postData } from "./user";

export async function requestStartRace() {
  const response = await fetch("http://localhost:4000/race/startRace");
  return response.json();
}

export async function requestEndRace(email: string, lpm: number, acc: number) {
  const response = await postData("http://localhost:4000/race/endRace", {
    email: email,
    runStats: {
      lpm: lpm,
      acc: acc,
    },
  });
  return response.json();
}
