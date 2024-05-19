import * as Yup from "yup";

const validationSchema = Yup.object({
   token: Yup.string().min(6).max(6).required()
});

export const activateAccountSchema = {
    fields: [
        {
            label: "Token",
            name: "token",
            as: "input"
        }
    ],
    validationSchema: validationSchema
}