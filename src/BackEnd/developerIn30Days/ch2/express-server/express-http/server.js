const express = require("express");
const app = express();
const port = 8080;

//use(가상 경로, static 폴더)
// localhost:8080/statci/img/1.png 는 public/img/1.png와 같음
// 정적 파일이 호스팅 되는 폴더에 대한 매핑임
app.use("/static", express.static("public"));

app.get("/", (req, res) => {
    res.send("<h1>Hello Wold!</h1>");
});

app.post("/", function (req, res) {
    res.send("Got a POST request");
});

app.listen(port, () => {
    console.log(`App listenting at http://localhost:${port}`);
});