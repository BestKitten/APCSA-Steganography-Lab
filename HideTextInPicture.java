import picturelib.Picture;

public class HideTextInPicture
{

  public static void main(String[] args)
  {
    Picture originalPic = new Picture("beach.png");
    originalPic.setTitle("Original, Unchanged Picture");
    originalPic.show();

    String secretMessage = "je ne sais pas";
    Picture hiddenMessagePic =
      Steganography.hideText(originalPic, secretMessage);
    hiddenMessagePic.setTitle("Picture With A Hidden Message");
    hiddenMessagePic.show();

    String revealedMessage = Steganography.revealText(hiddenMessagePic);
    System.out.println("Original Message: " + secretMessage
      + "\nRevealed Message: " + revealedMessage);
    System.out.println("Messages match: "
      + (secretMessage.toUpperCase().compareTo(revealedMessage) == 0));

  }

}
