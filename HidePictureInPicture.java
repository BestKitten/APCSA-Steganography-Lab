import picturelib.Picture;

public class HidePictureInPicture
{

  public static void main(String[] args)
  {
    // original image
    Picture originalHall = new Picture("femaleLionAndHall.png");
    originalHall.setTitle("Original, Unchanged Hall");
    originalHall.show();

    // pictures to hide
    Picture robot2 = new Picture("robot.png");
    Picture flower2 = new Picture("flower1.png");

    // hide pictures
    Picture newHall = Steganography.hidePicture(originalHall, robot2, 50, 300);
    newHall = Steganography.hidePicture(newHall, flower2, 115, 275);
    newHall.setTitle("Hall With Hidden Images");
    newHall.show();

    if (!Steganography.isSame(originalHall, newHall))
    {
      Picture hallDifferences =
        Steganography.showDifferentArea(originalHall, Steganography
          .findDifferences(originalHall, newHall));
      hallDifferences.setTitle("Difference Between Images");
      hallDifferences.show();

      Picture revealedImages = Steganography.revealPicture(newHall);
      revealedImages.setTitle("Previously Hidden Images");
      revealedImages.show();
    }

  }

}
