import { Button } from "@chakra-ui/react";
import { MdRefresh } from "react-icons/md";
import { useRef } from "react";

interface RestartProps {
  onRestart: () => void;
}
const RestartButton = (props: RestartProps) => {

    const buttonRef = useRef<HTMLButtonElement>(null);

    // prevents triggering restart button when starting to type
    const handleClick = () => {
        // unfocuses button after it is clicked
        buttonRef.current?.blur();
        props.onRestart();
    }


    return (
        <Button onClick={handleClick} ref={buttonRef}>Restart</Button>
    )
}

export default RestartButton;