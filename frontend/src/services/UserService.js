const API_URL = import.meta.env.VITE_API_URL;
const API_PORT = import.meta.env.VITE_API_PORT;

const UserService= {
    getUsersData: async function () {
        try {
          const response = await fetch(`${API_URL}:${API_PORT}/api/v1/users`);
          return await response.json();
        } catch (e) {
          throw new Error(e);
        }
    },
    createUser: async  function (user) {
        try {
            let userBody = {...user};
            const requestOptions = {
                method: 'POST',
                body: JSON.stringify(userBody),
                headers: {
                    "Content-Type": "application/json"
                }
            }
            await fetch(`${API_URL}:${API_PORT}/api/v1/users`, requestOptions)
        } catch (e) {
            throw new Error(e);
        }
    }
};

export default UserService;