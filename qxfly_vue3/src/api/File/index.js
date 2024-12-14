import request from "@/utils/request"; //@==>src

export const deleteFile = (file) => request.post("/deleteFile", file);
