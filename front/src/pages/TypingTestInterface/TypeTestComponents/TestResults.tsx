interface ResultProps{
    errors: number;
    accuracy: string | number;
    total: number;
}

const TestResults = (props:ResultProps) => {
    return (
      <ul>
        <li>Accuracy: {props.accuracy}</li>
        <li>Errors: {props.errors}</li>
        <li>Typed: {props.total}</li>
      </ul>
    );

}

export default TestResults;