const API_URL = import.meta.env.VITE_API_URL;
const API_PORT = import.meta.env.VITE_API_PORT;

const FileUploadService = {
    getFile: async function (filename) {
        try {
            const response = await fetch(`${API_URL}:${API_PORT}/api/v1/files/${filename}`);
            const blob = await response.blob();
            return URL.createObjectURL(blob);
        } catch (e) {
            throw new Error(e);
        }
    },
    uploadFile: async function (file, recipeID) {
        try {
            let body = new FormData();
            body.append('file', file);
            body.append('recipeId', recipeID);

            const requestOptions = {
                method: "POST",
                body: body
            }
            await fetch(`${API_URL}:${API_PORT}/api/v1/files`, requestOptions);
        } catch (e) {
            throw new Error(e);
        }
    }
};

export default FileUploadService;