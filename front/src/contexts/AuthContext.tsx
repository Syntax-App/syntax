import {
  User,
  signInWithEmailAndPassword,
  createUserWithEmailAndPassword,
  UserCredential,
  onAuthStateChanged,
  signOut,
  signInWithPopup,
  GoogleAuthProvider,
} from "firebase/auth";
import { FirebaseError } from "firebase/app";
import { getAuth } from "firebase/auth";
import { app } from "../config/firebase";
import React, { useContext, createContext, useState, useEffect } from "react";
import { requestCreateUser, requestGetUser } from "@/helpers/user";
import { UserStats } from "@/pages/profile";

export interface IAuthContext {
  currentUser: User | undefined;
  userInfo: IUserInfo | undefined;
  methods: IAuthMethods | undefined;
}

export interface IAuthMethods {
  emailLogin: () => Promise<void>;
  emailSignup: () => Promise<void>;
  googleLogin: () => Promise<void>;
  signout: () => Promise<void>;
}

export interface IUserInfo {
  uuid: string,
  name: string,
  email: string,
  pic: string,
  stats: UserStats
}

const emptyFunc = async () => {
  return;
};

const defaultState: IAuthContext = {
  currentUser: undefined,
  userInfo: undefined,
  methods: {
    emailLogin: emptyFunc,
    emailSignup: emptyFunc,
    googleLogin: emptyFunc,
    signout: emptyFunc,
  },
};

const AuthContext = createContext<IAuthContext>(defaultState);

export function useAuth() {
  return useContext(AuthContext);
}

export function AuthProvider({ children }: any) {
  const auth = getAuth(app);
  const [currentUser, setCurrentUser] = useState<User>();
  const [userInfo, setUserInfo] = useState<any>();
  const googleProvider = new GoogleAuthProvider();

  useEffect(() => {
    const unsubscribe = onAuthStateChanged(auth, (user) => {
      if (user) {
        setCurrentUser(user);
        if (user.email) {
          // There are two main cases where this will be called:
          //  1. The account already exists - requestGetUser will take care of getting its info.
          //  We verify, via the status field, that the account was obtained successfully.
          //  If it is, then we know we can update the userInfo state. We're all good.
          
          //  2. The most confusing state. Cost about two hours of debugging for Dan & Ayman
          //  The user signed up FOR THE FIRST TIME. onAuthStateChanged gets triggered AS SOON
          //  as the FIREBASE STATE gets updated. However, when it is, the handler for creating
          //  a new account WOULDN'T HAVE CALLED THE FUNCTION TO CREATE THE ACCOUNT ON THE DB YET!
          //  Therefore, requestGetUser WILL RETURN AN ERROR. If so, nothing happens, and we patiently
          //  wait for the handler to finish its job AND UPDATE USERINFO ITSELF. 
          requestGetUser(user.email)
          .then((r) => {
            if (r.status === "success") {
              setUserInfo(r.data.user);
            }
          })
        }
      } else {
        setCurrentUser(undefined);
      }
    });
    return unsubscribe;
  }, []);

  async function emailLogin() {
    return signInWithEmailAndPassword(
      auth,
      "daniel_liu2@brown.edu",
      "supersecurepassword"
    )
      .then((userCredentials: UserCredential) => {
        console.log("loggedin as " + userCredentials.user.email);
        setCurrentUser(userCredentials.user);
      })
      .catch((error: FirebaseError) => {
        console.log(error.code);
        console.log(error.message);
      });
  }

  async function emailSignup() {
    return createUserWithEmailAndPassword(
      auth,
      "daniel_liu2@brown.edu",
      "supersecurepassword"
    )
      .then((userCredentials: UserCredential) => {
        console.log("signedup as " + userCredentials.user.email);
        setCurrentUser(userCredentials.user);
      })
      .catch((error: FirebaseError) => {
        console.log(error.code);
        console.log(error.message);
      });
  }

  async function googleLogin() {
    return signInWithPopup(auth, googleProvider)
      .then((result) => {
        if (
          result.user.displayName &&
          result.user.email &&
          result.user.photoURL
        ) {
          requestCreateUser(
            result.user.displayName,
            result.user.email,
            result.user.photoURL
          ).then((userInf) => {
            console.log(userInf);
            setUserInfo(userInf.data.user);
          });
        }
        //setCurrentUser(result.user);
      })
      .catch((error: FirebaseError) => {
        console.log(error.code);
        console.log(error.message);
      });
  }

  async function signout() {
    setUserInfo(undefined);
    return signOut(auth);
  }

  const value: IAuthContext = {
    currentUser: currentUser,
    userInfo: userInfo,
    methods: { emailLogin, emailSignup, googleLogin, signout },
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}
