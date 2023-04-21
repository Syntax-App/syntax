export const countErrors = (actual: string, expected: string) => {
    const expectedCharacters = expected.split("");

    return expectedCharacters.reduce((errors, expectedChar, i) => {
        const actualChar = actual[i];
        if (actualChar !== expectedChar){
            errors++; 
        }

        return errors;

    }, 0);
};

export const calculateAccuracy = (errors: number, totalChars: number) => {
    if (totalChars > 0){
        const correct = totalChars-errors;
        return ((correct / totalChars) * 100).toFixed(1) + "%";
    }

    return 100 + "%";
}