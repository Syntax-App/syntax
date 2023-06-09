import { useColorModeValue } from "@chakra-ui/react";
import { motion } from "framer-motion";

const Caret = () => {
  return (
    <motion.div
      className="caret"
      id={useColorModeValue("caretlight", "caretdark")}
      aria-hidden={true}
      initial={{ opacity: 1 }} // blinking effect
      animate={{ opacity: 0 }}
      exit={{ opacity: 1 }}
      transition={{ repeat: Infinity, duration: 0.8, ease: "easeInOut" }}
    />
  );
};

export default Caret;
