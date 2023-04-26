import UserType from "../pages/TypingTestInterface/TypeTestComponents/UserType";

interface WordsProps{
    userInput: string;
    words: string; // this props.code is for random generated
    typeMode: boolean;
}

export default function WordsContainer(props: WordsProps) {
    return (
        <div
        className="typetest"
        autoFocus={true}
        onBlur={({ target }) => target.focus()}
        >
        <div
            className="usertyped"
            onKeyDown={(e) => {
            if (e.code === "Tab") {
                e.preventDefault();
                console.log("tabbed");
            }
            }}
        >
            <pre>
            <code>
                <UserType
                userInput={props.userInput}
                words={props.words}
                typeMode={props.typeMode}
                />
            </code>
            </pre>
        </div>
        <div className="codesnippet">
            <pre>
            <code>{props.words}</code>
            </pre>
        </div>
        </div>
    );
}