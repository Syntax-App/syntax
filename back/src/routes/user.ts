import {
  collection,
  addDoc,
  getDoc,
  getDocs,
  query,
  where,
  QueryDocumentSnapshot,
  DocumentData,
} from "firebase/firestore";
import { db } from "../config/firebase";
import { UserInfo } from "../types/user";
import crypto from "crypto";

export const createUser = async (req: any, res: any) => {
  const usersRef = collection(db, "users");
  const userQuery = query(usersRef, where("email", "==", req.body.email));
  const querySnapshot = await getDocs(userQuery);

  if (querySnapshot.docs.length) {
    return res.status(200).send({
      status: "error",
      message: "User with given email already exists!",
    });
  }

  const userObject: UserInfo = {
    uuid: crypto.randomUUID(),
    name: req.body.name,
    email: req.body.email,
    pic: req.body.pic,
    stats: {
      highestLPM: 0,
      highestAccuracy: 0,
      avgAccuracy: 0,
      avgLPM: 0,
      totalRaces: 0,
    },
  };
  addDoc(usersRef, userObject)
    .then(() => {
      return res.status(200).send({
        status: "success",
        user: userObject,
      });
    })
    .catch((e: Error) => {
      return res.status(200).send({ status: "error", message: e.message });
    });
};

export const getUser = async (req: any, res: any) => {
  const email = req.query.email;

  const usersRef = collection(db, "users");
  const userQuery = query(usersRef, where("email", "==", email));
  const querySnapshot = await getDocs(userQuery);

  if (!querySnapshot.docs.length) {
    return res.status(200).send({
      status: "error",
      message: "User with given email does not exist!",
    });
  } else {
    return res
      .status(200)
      .send({ status: "success", user: querySnapshot.docs[0].data() });
  }

  // todo - check for existence
};
