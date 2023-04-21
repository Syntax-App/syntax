import { FirebaseApp, FirebaseOptions, getApp, getApps, initializeApp } from "firebase/app";
import { getFirestore } from "firebase/firestore";

const firebaseConfig : FirebaseOptions = {
    apiKey: "AIzaSyC_f_Roxody9SJuO0MWyTt59i2lcOLlhmI",
    authDomain: "syntax-dev-45ff7.firebaseapp.com",
    projectId: "syntax-dev-45ff7",
    storageBucket: "syntax-dev-45ff7.appspot.com",
    messagingSenderId: "679188862445",
    appId: "1:679188862445:web:aadec32a94a5b44a01c3e4"
};

function firebaseInit() {
    let app: FirebaseApp;

    if (getApps().length) {
        app = getApp();
    } else {
        app = initializeApp(firebaseConfig);
    }
    
    return app;
}

export const db = getFirestore(firebaseInit());