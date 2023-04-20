import express from "express";
import bp from "body-parser";

const app = express();
app.use(bp.json());

app.get("/", async (req, res) => {
  res
    .status(200)
    .send({ message: "Hello, world!", example: { name: req.query.name, age: 20 } });
});

app.listen(4000, () => console.log("listening on port " + 4000))
