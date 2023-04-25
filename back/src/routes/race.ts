import {
  collection,
  query,
  where,
  getDocs,
  updateDoc,
  doc,
} from "firebase/firestore";
import { db } from "../config/firebase";
import fs from "fs";
import { UserInfo, isUserInfo } from "../types/user";

export const startRace = (req: any, res: any) => {
  // get code snippet
  const fileContent = fs.readFileSync("src/ReactFlightClient.js");
  return res.status(200).send({
    status: "success",
    data: { snippet: fileContent.toString() },
  });
};

export const endRace = async (req: any, res: any) => {
  const runData = req.body.runStats;

  const usersRef = collection(db, "users");
  const userQuery = query(usersRef, where("email", "==", req.body.email));
  const querySnapshot = await getDocs(userQuery);

  const userObject: any = querySnapshot.docs[0].data();

  if (!isUserInfo(userObject)) {
    // just error out or some shit
    return;
  }

  if (runData.lpm > userObject.stats.highlpm) {
    userObject.stats.highlpm = runData.lpm;
  }
  if (runData.acc > userObject.stats.highacc) {
    userObject.stats.highacc = runData.acc;
  }

  userObject.stats.avgacc =
    (userObject.stats.avgacc * userObject.stats.numraces + runData.acc) /
    (userObject.stats.numraces + 1);
  userObject.stats.avglpm =
    (userObject.stats.avglpm * userObject.stats.numraces + runData.lpm) /
    (userObject.stats.numraces + 1);
  userObject.stats.numraces++;

  updateDoc(doc(db, "users", querySnapshot.docs[0].id), userObject)
    .then(() => {
      return res.status(200).send({
        status: "success",
        data: { user: userObject },
      });
    })
    .catch((e: Error) => {
      return res.status(200).send({ status: "error", message: e.message });
    });
};
