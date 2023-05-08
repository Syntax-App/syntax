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
import { requestCreateUser, requestGetUser, requestUpdateUserStats } from "@/helpers/user";
import { UserStats } from "@/pages/profile";

export interface IAuthContext {
  currentUser: User | undefined;
  userInfo: IUserInfo | undefined;
  methods: IAuthMethods | undefined;
  loading: boolean;
}

export interface IAuthMethods {
  emailLogin: (email : string, password : string) => Promise<void>;
  emailSignup: (displayName : string, email : string, password : string) => Promise<void>;
  googleLogin: () => Promise<void>;
  signout: () => Promise<void>;
  updateUserStats: (recentlpm: number, recentacc: number) => Promise<void>;
}

export interface IUserInfo {
  uuid: string;
  name: string;
  email: string;
  pic: string;
  stats: UserStats;
}

export interface IUserInfoResponse {
  data: {user: IUserInfo}
}

const emptyFunc = async () => {
  return;
};

const defaultState: IAuthContext = {
  currentUser: undefined,
  userInfo: undefined,
  loading: false,
  methods: {
    emailLogin: emptyFunc,
    emailSignup: emptyFunc,
    googleLogin: emptyFunc,
    signout: emptyFunc,
    updateUserStats: emptyFunc,
  },
};

const AuthContext = createContext<IAuthContext>(defaultState);

export function useAuth() {
  return useContext(AuthContext);
}

export function AuthProvider({ children }: any) {
  const auth = getAuth(app);
  const [currentUser, setCurrentUser] = useState<User>();
  const [userInfo, setUserInfo] = useState<IUserInfo>();
  const googleProvider = new GoogleAuthProvider();
  const [loading, setloading] = useState<boolean>(true)

  useEffect(() => {
    const unsubscribe = onAuthStateChanged(auth, (user) => {
      setloading(true);
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
          requestGetUser(user.email).then((r) => {
            if (r.status === "success" && isUserDataSuccessResponse(r.data.user)) {
              setUserInfo(r.data.user);
              setloading(false);
            } else {
              console.log("Invalid user data info.")
            }
          });
        }
      } else {
        setCurrentUser(undefined);
        setloading(false);
      }
    });
    return unsubscribe;
  }, []);

  async function emailLogin(email : string, password : string) {
    return signInWithEmailAndPassword(
      auth,
      email,
      password
    )
      .then((userCredentials: UserCredential) => {
        requestGetUser(email)
        .then((userInf : IUserInfoResponse) => {
          if (isUserDataSuccessResponse(userInf.data.user)){
          setUserInfo(userInf.data.user);
          } else {
            console.log("Attempted email login with invalid UserInfo.")
          }
        })
      })
      .catch((error: FirebaseError) => {
        console.log(error.code);
        console.log(error.message);
      });
  }

  async function emailSignup(displayName : string, email : string, password : string) {
    return createUserWithEmailAndPassword(
      auth,
      email,
      password
    )
      .then((userCredentials: UserCredential) => {
        requestCreateUser(
          displayName,
          email,
          undefined // will use default profile picture.
        ).then((userInf : IUserInfoResponse) => {
          setUserInfo(userInf.data.user);
        }) 
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
          ).then((userInf : IUserInfoResponse) => {
            setUserInfo(userInf.data.user);
          });
        }
      })
      .catch((error: FirebaseError) => {
        console.log(error.code);
        console.log(error.message);
      });
  }

  async function updateUserStats(recentlpm: number, recentacc: number) {
    //TODO: decide what to do if user is a guest
    if (userInfo === undefined) return;
    // update stats if logged in
    return requestUpdateUserStats(userInfo.email, recentlpm, recentacc).then((res) => {
      console.log(res);
    });
  }

  async function signout() {
    setUserInfo(undefined);
    return signOut(auth);
  }

  function isUserDataSuccessResponse(json: any): json is IUserInfo {
    if (!("uuid" in json)) return false;
    if (!("name" in json)) return false;
    if (!("email" in json)) return false;
    if (!("pic" in json)) return false;
    if (!("stats" in json)) return false;

    return true;
  }

  const value: IAuthContext = {
    currentUser: currentUser,
    userInfo: userInfo,
    loading: loading,
    methods: { emailLogin, emailSignup, googleLogin, signout, updateUserStats },
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}
