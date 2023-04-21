import { Button, IconButton, Text, Flex} from "@chakra-ui/react";
import { MdRefresh } from "react-icons/md";
import { useRef } from "react";
import { VscDebugRestart } from "react-icons/vsc";

interface RestartProps {
  onRestart: () => void;
  typeMode: boolean;
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
      <Flex
        display={props.typeMode ? "flex" : "none"}
        flexDirection={"column"}
        alignItems={"center"}
        justifyContent={"center"}
      >
        <IconButton
          aria-label="Refresh Button"
          onClick={handleClick}
          ref={buttonRef}
          size={"lg"}
          color={"#77A3CD"}
          bg="none"
          justifyContent={"center"}
          alignItems={"center"}
          icon={<VscDebugRestart />}
        ></IconButton>
        <Text variant={"label"}>
          
          RESTART
        </Text>
      </Flex>
    );
}

export default RestartButton;