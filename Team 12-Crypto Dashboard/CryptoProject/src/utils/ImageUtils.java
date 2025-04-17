package utils;

import java.util.Base64;
import java.io.FileOutputStream;
import java.io.File;

public class ImageUtils {
    public static void main(String[] args) {
        // Base64 string of the default crypto icon
        String base64Image = "iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyRpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDcuMi1jMDAwIDc5LjFiNmZhNzksIDIwMjIvMDYvMTMtMjI6MDE6MDEgICAgICAgICI+IDxyZGY6UkRGIHhtbG5zOnJkZj0iaHR0cDovL3d3dy53My5vcmcvMTk5OS8wMi8yMi1yZGYtc3ludGF4LW5zIyI+IDxyZGY6RGVzY3JpcHRpb24gcmRmOmFib3V0PSIiIHhtbG5zOnhtcD0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wLyIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bXA6Q3JlYXRvclRvb2w9IkFkb2JlIFBob3Rvc2hvcCAyNC4wIChXaW5kb3dzKSIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDpFNzk3RjdGMkY5NDYxMUVEQTM1QUNEOTFDRjY0QkNGMSIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDpFNzk3RjdGM0Y5NDYxMUVEQTM1QUNEOTFDRjY0QkNGMSI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOkU3OTdGN0YwRjk0NjExRURBMzVBQ0Q5MUNGNjRCQ0YxIiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOkU3OTdGN0YxRjk0NjExRURBMzVBQ0Q5MUNGNjRCQ0YxIi8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+aEqvwQAABKhJREFUeNq8V1tsVFUUXfe8O522M522004fGKDAQCstCgR5mBIiYIgmfsQYwUBCCPrx74+JMSY+4oea+IGJiRr5EDWYaIgPBI1BQKQU6IvS0k6n87zv+37MvaeEYmJqaePtyczMvWfttc8+e599z1X09fUxzIBx1myc2TobC0vyEU0wGFZ5rTA4JbZxunPgrrJm+iTSfPs378GKykqogp6+7sAj049DLeagS+6d+bQhItrsucoDlkRiXKX1FMbirVvtcBYVJE0YSMwQgdVigsFghIqjkM6kM6yKQYhPQZnKwJWTj7KcfFQVu+ExWMAwaWQ4LdQMjVHvGLzBIHiiISgGvBZoVQRdA91IksCJuMIo8gBc+C9Y9pAfQ33D4FQqZFKSlKmUDLlJhTUuMxZPMRD0HIoCBApLCtB+qhWeoSGQHAWj1YRVtQ1YWLnE4R8eOKJiGS6bCIZUKpVtqr19fX2/s13d3VBQFNiUVqfWsWp9WlAqFPBPTuLy5Q443S5QJIN4IoFgcBJJPgmWZZFIJTA5OQGDXo+auioMekbBqBmQJC7jQDjCIxISIKXlsfJuhCSQFIXGxkbsP3AQZz5tQXVFFQK+ccQSAhg1jYrqGoyN+uAPBZFbtxgvPlOHT1pOgKbV4HkaLM2AEEVweIbfTrrW1tZyHZ1dB8iXD741JJ7iqv8h0AQHnqLgn4pg1O+HxaBHu8eP3kEvVhfn4rGSPAQEEpFoAqePvAHfqB/dY2MgGQ4qkoZOrYYgCNiw6Wm0njyBytoazHfegSg7gZ5BH/p9Y+m4GG1giUR7a2trQl7qgmWeQc4fwKRGA4bWwGDzwGpdhETMD7VeB9+YF3VOO75q6USBXoOAEAYXjqGpvhrLVy/D5MQEQj4/qubPw8PVNXh0VT2UUkVIpMAqKdQ9UotD3x5G25lTmBfPwELGUa4BNi5wwh/24cyAH7wQZq+fPztc39DQMFRVVYXKykrs2LkT3d3dL8oEysuKUWezYJgEHnS4kVdVgA11Vbg44sdwUIDObsHdSQBiigcvJJGQRITjSRg1NJasrcflvuuQFAr0DQ3ghosX0dlxEd82H8ayB5fCKibAnOk+iwnvCLweFxKJBJqamq7LBGw2GyiJxMEDB5Gbk49LnkG89+EuFLjKYbZa0L53D5I8j+1bN6O8tAQirUEsEUc0TaLIakbzZ81YtaEJuckAAiEf3tn+IYrsVmz/+D14QlNIcvIi2MgZVg4J3OqoMozETVMAI7IoakRTEhgthbWPrIXFYvmLCe3fv3+3fOPoBx+BJGR2CVEPgiChkF14sRgULI0UmUAoHsckvwS5uQUIRoKgZELqFALxFEZ8o6BJEisb1yGVokEpKKlC4RHww4/H8HpjA347fhJ7jndjzaaNmJishP/ECbTs3j1jeC4tLfXJJ4UnTgwM9P+yolTgg5fXp3a8/w5VbLdj967PU++9+SZtMlmwaePzqePHjmFpzRIwWi0+2f0p1q5fj1RKwN4vv0LLl7tBUwqIOgZ19Q0gciVEnSQa1qzGsZbDv69eseqQ7P9DrRWGJUWTtV2trbqdH+0AkQphQmAQF8L/bv0s5bWxou+xCZbQUUFRlPMmiw/4CQqTShT+7XfhpwADAFYuTwO7KyXvAAAAAElFTkSuQmCC";

        try {
            // Create directories if they don't exist
            File dir = new File("c:\\Users\\Asus\\Desktop\\CryptoProject\\src\\resources\\images");
            dir.mkdirs();

            // Convert base64 to image and save
            byte[] decodedBytes = Base64.getDecoder().decode(base64Image);
            String filePath = "c:\\Users\\Asus\\Desktop\\CryptoProject\\src\\resources\\images\\default-crypto.png";

            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                fos.write(decodedBytes);
                System.out.println("Image saved successfully to: " + filePath);
            }
        } catch (Exception e) {
            System.err.println("Error saving image: " + e.getMessage());
        }
    }
}
