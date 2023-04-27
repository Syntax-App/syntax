export const countErrors = (actual: string, expected: string) => {
  const expectedCharacters = expected.split("");

  return expectedCharacters.reduce((errors, expectedChar, i) => {
    const actualChar = actual[i];
    if (actualChar !== expectedChar) {
      errors++;
    }

    return errors;
  }, 0);
};

// export const countCompletedLines = (expected: string) => {
//   const expectedCharacters = expected.split("");


//   return expectedCharacters.reduce((errors, expectedChar, i) => {
   
//     if (expectedChar === "\n") {
//         console.log("MATCH");
//       errors++;
//     }

//     return errors;
//   }, 0);
// };

export const calculateAccuracy = (errors: number, totalChars: number) => {
  if (totalChars > 0) {
    const correct = totalChars - errors;
    return Math.round((correct / totalChars) * 100);
  }

  return 100;
};
