import * as Yup from "yup";

const validationSchema = Yup.object({
    username:  Yup.string().max(30, "Username must be less than 30 characters").required(),
    password: Yup.string().min(6).max(30).required()
});

export const loginSchema = {
    fields: [
        {
            label: "Username",
            name: "username",
            as: "input"
        },
        {
            label: "Password",
            name: "password",
            as: "input",
            type: "password"
        }
    ],
    validationSchema: validationSchema
}