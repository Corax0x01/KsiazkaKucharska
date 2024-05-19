const API_URL = import.meta.env.VITE_API_URL;
const API_PORT = import.meta.env.VITE_API_PORT;

const AuthenticationService = {
    register: async function (registerInfo) {
        try {
            const registerBody = {...registerInfo};
            const requestOptions = {
                method: "POST",
                body: JSON.stringify(registerBody),
                headers: {
                    "Content-Type": "application/json"
                }
            };
            await fetch(
                `${API_URL}:${API_PORT}/api/v1/auth/register`,
                requestOptions
            );
        } catch (e) {
            throw new Error(e);
        }
    },
    activateAccount: async function (token) {
      try {
          await fetch(
              `${API_URL}:${API_PORT}/api/v1/auth/activate-account?token=${token}`
          );
      }  catch (e) {
          throw new Error(e);
      }
    },
    logIn: async function (credentials) {
        try {
            let loginBody = {...credentials};

            const requestOptions = {
                method: "POST",
                body: JSON.stringify(loginBody),
                headers: {
                    "Content-Type": "application/json"
                }
            };
            const response = await fetch(`${API_URL}:${API_PORT}/api/v1/auth/authenticate`, requestOptions);
            const json = await response.json();
            console.log(json["token"])
            return json["token"];
        } catch (e) {
            throw new Error(e);
        }
    }
}

export default AuthenticationService;