export async function requestCreateUser(
  name: string,
  email: string,
  pic: string | undefined
) {
  const response = await postData("http://localhost:4000/user/create", {
    name: name,
    email: email,
    pic: pic
      ? pic
      : "https://upload.wikimedia.org/wikipedia/commons/9/99/Sample_User_Icon.png",
  });
  return response.json();
}

export async function requestUpdateUserStats(
  email: String,
  recentlpm: number,
  recentacc: number
) {
  const response = await postData("http://localhost:4000/race/end", {
    email: email,
    recentlpm: recentlpm,
    recentacc: recentacc,
  });

  return response.json();
}

export async function requestGetUser(email: string) {
  const response = await fetch("http://localhost:4000/user/get?email=" + email);
  return await response.json();
}

export async function requestRankings() {
  const response = await fetch("http://localhost:4000/user/ranking");
  const json = await response.json();
  return (
    json.data.ranking
  );
}

export async function requestCode(lang: string, email?: string) {
  const response = await fetch("http://localhost:4000/race/start?email=" + email + "&lang=" + lang);
  const json = await response.json();
  return (
    json.data.snippet
  );
}

// copied from https://developer.mozilla.org/en-US/docs/Web/API/Fetch_API/Using_Fetch
async function postData(url = "", data = {}) {
  // Default options are marked with *
  const response = await fetch(url, {
    method: "POST", // *GET, POST, PUT, DELETE, etc.
    mode: "cors",
    //mode: "no-cors", // no-cors, *cors, same-origin
    cache: "no-cache", // *default, no-cache, reload, force-cache, only-if-cached
    credentials: "same-origin", // include, *same-origin, omit
    headers: {
      "Content-Type": "application/json",
      ///'Content-Type': 'application/x-www-form-urlencoded',
    },
    redirect: "follow", // manual, *follow, error
    referrerPolicy: "no-referrer", // no-referrer, *no-referrer-when-downgrade, origin, origin-when-cross-origin, same-origin, strict-origin, strict-origin-when-cross-origin, unsafe-url
    body: JSON.stringify(data), // body data type must match "Content-Type" header
  });
  return response; // parses JSON response into native JavaScript objects
}
