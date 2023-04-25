import express from "express";
import bp from "body-parser";
import { createUser, getUser } from "./routes/user";
import { startRace } from "./routes/race";

const app = express();
app.use(bp.json());
app.use(function (req, res, next) {
  res.header("Access-Control-Allow-Origin", "*");
  res.header(
    "Access-Control-Allow-Headers",
    "Origin, X-Requested-With, Content-Type, Accept"
  );
  next();
});
// app.use(bp.urlencoded({ extended: true }));

app.get("/", async (req, res) => {
  res.status(200).send({
    message: "Hello, world!",
    example: { name: req.query.name, age: 20 },
  });
});

app.get("/user/get", getUser);
app.post("/user/create", createUser);
app.get("/race/start", startRace);

app.listen(4000, () => console.log("listening on port " + 4000));
