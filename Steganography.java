import java.awt.Color;
import java.util.ArrayList;

import picturelib.Picture;
import picturelib.Pixel;
import picturelib.Point;

public class Steganography
{
  /**
   * Clear the lower (rightmost) two bits in a pixel.
   */
  public static void clearLow(Pixel aSinglePixel)
  {
    aSinglePixel.setRed((aSinglePixel.getRed() / 4) * 4);
    aSinglePixel.setBlue((aSinglePixel.getBlue() / 4) * 4);
    aSinglePixel.setGreen((aSinglePixel.getGreen() / 4) * 4);
  }

  /**
   * Set the lower 2 bits in a pixel to the highest 2 bits in c
   */
  public static void setLow(Pixel aSinglePixel, Color anRGBColor)
  {
    clearLow(aSinglePixel);
    Color anRGBColorClear6 =
      new Color(aSinglePixel.getRed() + anRGBColor.getRed() / 64,
                aSinglePixel.getGreen() + anRGBColor.getGreen() / 64,
                aSinglePixel.getBlue() + anRGBColor.getBlue() / 64);
    aSinglePixel.setColor(anRGBColorClear6);
  }

  /**
   * Sets the highest two bits of each pixel’s colors * to the lowest two bits
   * of each pixel’s colors. This will send the secret picture back that was
   * hidden in the picture named "hidden".
   */
  public static Picture revealPicture(Picture hidden)
  {
    Picture copy = new Picture(hidden);
    Pixel[][] pixels = copy.getPixels2D();
    Pixel[][] source = hidden.getPixels2D();
    for (int r = 0; r < pixels.length; r++)
    {
      for (int c = 0; c < pixels[0].length; c++)
      {
        Color col = source[r][c].getColor();
        Color newColor =
          new Color((col.getRed() % 4) * 64, (col.getGreen() % 4) * 64,
                    (col.getBlue() % 4) * 64);

        pixels[r][c].setColor(newColor);
      }
    }

    return copy;
  }

  /**
   * Determines whether secret can be hidden in source
   * 
   * @param source is not null
   * @param secret is not null
   * 
   * @return true if secret can be hidden in source, false otherwise. * true if
   *         source and secret are the same (OR less) dimensions.
   */
  public static boolean canHide(Picture source, Picture secret)
  {
    return source.getHeight() >= secret.getHeight()
      && source.getWidth() >= secret.getWidth();
  }

  /**
   * Creates a new Picture with data from secret hidden in data from source
   * 
   * @param source  is not null
   * @param secret is not null
   * 
   * @return combined Picture with secret hidden in source precondition: source
   *         is same width and height as secret
   */
  public static Picture hidePicture(Picture source, Picture secret)
  {
    Picture hidden = new Picture(source);
    Pixel[][] hiddenPixels = hidden.getPixels2D();
    Pixel[][] secretPixels = secret.getPixels2D();

    for (int r = 0; r < secretPixels.length; r++)
    {
      for (int c = 0; c < secretPixels[0].length; c++)
      {
        setLow(hiddenPixels[r][c], secretPixels[r][c].getColor());
      }
    }

    return hidden;
  }

  /**
   * Hides secret in picture, starting at a given point in picture
   * 
   * @param source is not null
   * @param secret is not null
   * @param startRow row in source where hidden pic will start
   * @param startColumn column in source where hidden pic will start
   * 
   * @return combined Picture with secret hidden in source precondition: source
   *         is same or greater width and height as secret
   */
  public static Picture hidePicture(Picture source, Picture secret,
                                    int startRow, int startColumn)
  {
    Picture hidden = new Picture(source);
    Pixel[][] hiddenPixels = hidden.getPixels2D();
    Pixel[][] secretPixels = secret.getPixels2D();

    for (int r = 0; r < secretPixels.length; r++)
    {
      for (int c = 0; c < secretPixels[0].length; c++)
      {
        setLow(hiddenPixels[r + startRow][c + startColumn],
               secretPixels[r][c].getColor());
      }
    }

    return hidden;
  }

  /**
   * Checks to see if pic1 and pic2 match
   * 
   * @param pic1  first picture
   * @param pic2 second picture
   * 
   * @return true if pics match; false otherwise
   */
  public static boolean isSame(Picture pic1, Picture pic2)
  {
    Pixel[][] pixels = pic1.getPixels2D();
    Pixel[][] otherPixels = pic2.getPixels2D();
    boolean picturesMatch = true;

    if (pixels.length == otherPixels.length
      && pixels[0].length == otherPixels[0].length)
    {
      for (int r = 0; r < pixels.length; r++)
      {
        for (int c = 0; c < pixels[0].length; c++)
        {
          Pixel pixel = pixels[r][c];
          Pixel otherPixel = otherPixels[r][c];

          if (pixel.getRed() != otherPixel.getRed()
            || pixel.getGreen() != otherPixel.getGreen()
            || pixel.getBlue() != otherPixel.getBlue())
          {
            picturesMatch = false;
          }
        }
      }
    }
    else
    {
      picturesMatch = false;
    }

    return picturesMatch;
  }

  /**
   * Returns arraylist of points where pictures differ
   * 
   * @param pic1 first picture
   * @param pic2 second picture
   * 
   * @return list of points where pic1 and pic2 differ or * returns empty list
   *         if they are not the same size
   */
  public static ArrayList<Point> findDifferences(Picture pic1, Picture pic2)
  {
    ArrayList<Point> list = new ArrayList<>();

    Pixel[][] pixels = pic1.getPixels2D();
    Pixel[][] otherPixels = pic2.getPixels2D();

    if (pixels.length == otherPixels.length
      && pixels[0].length == otherPixels[0].length)
    {
      for (int r = 0; r < pixels.length; r++)
      {
        for (int c = 0; c < pixels[0].length; c++)
        {
          Pixel pixel = pixels[r][c];
          Pixel otherPixel = otherPixels[r][c];

          if (pixel.getRed() != otherPixel.getRed()
            || pixel.getGreen() != otherPixel.getGreen()
            || pixel.getBlue() != otherPixel.getBlue())
          {
            list.add(new Point(r, c));
          }
        }
      }
    }

    return list;
  }

  /**
   * Draws red rectangle around area containing points
   * 
   * @param pic source picture
   * @param points list of points
   * 
   * @return pic with rectangle drawn pre-condition all of points are on the
   *         Picture pic * pre-condition - points contains at least two points
   */
  public static Picture showDifferentArea(Picture pic, ArrayList<Point> points)
  {
    Picture result = new Picture(pic);
    Pixel[][] pixelsOfResult = result.getPixels2D();

    int boundingX1 = points.get(0).getCol();
    int boundingX2 = points.get(points.size() - 1).getCol();
    int boundingY1 = points.get(0).getRow();
    int boundingY2 = points.get(points.size() - 1).getRow();

    for (int r = boundingY1; r <= boundingY2; r++)
    {
      pixelsOfResult[r][boundingX1].setColor(new Color(255, 0, 0));
    }

    for (int r = boundingY1; r <= boundingY2; r++)
    {
      pixelsOfResult[r][boundingX2].setColor(new Color(255, 0, 0));
    }

    for (int c = boundingX1; c <= boundingX2; c++)
    {
      pixelsOfResult[boundingY1][c].setColor(new Color(255, 0, 0));
    }

    for (int c = boundingX1; c <= boundingX2; c++)
    {
      pixelsOfResult[boundingY2][c].setColor(new Color(255, 0, 0));
    }

    return result;
  }

  /**
   * Takes a string consisting of letters and spaces and encodes the string into
   * an arraylist of integers. The integers are 1-26 for A-Z, 27 for space, and
   * 0 for end of string. The arraylist of integers is returned. Call this
   * method in hideText
   * 
   * @param topSecretTextMessage string consisting of letters and spaces
   * 
   * @return ArrayList containing integer encoding of uppercase
   */
  public static ArrayList<Integer> encodeString(String topSecretTextMessage)
  {
    String upperCaseTopSecretTextMessage = topSecretTextMessage.toUpperCase();
    String alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    ArrayList<Integer> result = new ArrayList<Integer>();
    for (int i = 0; i < upperCaseTopSecretTextMessage.length(); i++)
    {
      String aLetter = upperCaseTopSecretTextMessage.substring(i, i + 1);
      if (aLetter.equals(" "))
      {
        result.add(27);
      }
      else
      {
        result.add(alpha.indexOf(aLetter) + 1);
      }
    }
    result.add(0);
    return result;
  }

  /**
   * Returns the string represented by the codes arraylist. * 1-26 = A-Z, 27 = space
   * 
   * @param codes encoded string
   * 
   * @return decoded string
   */
  public static String decodeString(ArrayList<Integer> codes)
  {
    String result = "";
    String alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    for (int i = 0; i < codes.size(); i++)
    {
      if (codes.get(i) == 27)
      {
        result = result + " ";
      }
      else
      {
        result = result + alpha.substring(codes.get(i) - 1, codes.get(i));
      }
    }
    return result;
  }

  /**
   * Given a number from 0 to 63, creates and returns a 3-element * int array
   * consisting of the integers representing the pairs of bits in the number
   * from right to left.
   * 
   * @param num number to be broken up
   * 
   * @return bit pairs in number
   */
  private static int[] getBitPairs(int num)
  {
    int[] bits = new int[3];
    int code = num;
    for (int i = 0; i < 3; i++)
    {
      bits[i] = code % 4;
      code = code / 4;
    }
    return bits;
  }

  /**
   * Hide a string (must be only capital letters and spaces) in a * picture. The
   * string always starts in the upper left corner.
   * 
   * @param source picture to hide string in
   * @param topSecretTextMessage string to hide
   * 
   * @return picture with hidden string
   */
  public static Picture hideText(Picture source, String topSecretTextMessage)
  {
    Picture newPic = new Picture(source);
    Pixel[][] pixels = newPic.getPixels2D();
    ArrayList<Integer> encodedMessage = encodeString(topSecretTextMessage);

    boolean hiddenFullMessage = false;
    for (int r = 0; r < pixels.length && !hiddenFullMessage; r++)
    {
      for (int c = 0; c < pixels[0].length && !hiddenFullMessage; c++)
      {
        if (r + c < encodedMessage.size())
        {
          int[] bits = getBitPairs(encodedMessage.get(r + c));
          Pixel aSinglePixel = pixels[r][c];
          clearLow(aSinglePixel);

          Color anRGBColorClear6 =
            new Color(aSinglePixel.getRed() + bits[0],
                      aSinglePixel.getGreen() + bits[1],
                      aSinglePixel.getBlue() + bits[2]);
          aSinglePixel.setColor(anRGBColorClear6);
        }
        else
        {
          hiddenFullMessage = true;
        }

      }
    }

    return newPic;
  }

  /**
   * Returns a string hidden in the picture
   * 
   * @param source picture with hidden string *
   * 
   * @return revealed string
   */
  public static String revealText(Picture source)
  {
    Pixel[][] pixels = source.getPixels2D();
    ArrayList<Integer> codes = new ArrayList<Integer>();

    boolean revealedFullMessage = false;
    for (int r = 0; r < pixels.length && !revealedFullMessage; r++)
    {
      for (int c = 0; c < pixels[0].length && !revealedFullMessage; c++)
      {
        Color col = pixels[r][c].getColor();
        int code =
          (col.getRed() % 4) + (col.getGreen() % 4) * 4
            + (col.getBlue() % 4) * 16;

        if (code != 0)
        {
          codes.add(code);
        }
        else
        {
          revealedFullMessage = true;
        }
      }
    }

    return decodeString(codes);
  }
}
