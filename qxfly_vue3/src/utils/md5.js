import md5 from "js-md5";
let salt = "";
function generateMD5(data) {
  var a = data;
  return md5(a);
}
function generateMD5BySalt(data) {
  let nums = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, "a", "b", "c", "d", "e", "f"];
  salt = "";
  for (var i = 0; i < 6; i++) {
    salt += nums[Math.floor(Math.random() * nums.length)];
  }
  const md5Hash = md5(data + salt);
  return md5Hash;
}

function getSalt() {
  return salt;
}
export default { generateMD5BySalt, generateMD5, getSalt };
