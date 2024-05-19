import * as Yup from "yup";

const validationSchema = Yup.object({
    username: Yup.string().required(),
    email: Yup.string().email().required(),
    password: Yup.string().min(6).max(20).required(),
    confirmPassword:Yup.string().required().oneOf(
        [Yup.ref("password")],
        "Passwords must match"
    )
});

export const registerSchema = {
    fields: [
        {
            label: "Username",
            name: "username",
            as: "input"
        },
        {
            label: "Email",
            name: "email",
            as: "input",
            type: "email"
        },
        {
            label: "Password",
            name: "password",
            as: "input",
            type: "password"
        },
        {
            label: "Confirm password",
            name: "confirmPassword",
            as: "input",
            type: "password"
        }
    ],
    validationSchema: validationSchema
}