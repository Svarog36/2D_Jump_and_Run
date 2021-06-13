class CollisionDetection {

    static boolean collisionDetectionRectangle(double x1, double y1, double width1, double height1, double x2, double y2, double width2, double height2) {
        if ((x1 + width1 > x2 && x1 + width1 < x2 + width2) || (x1 < x2 + width2 && x1 > x2))
            return (y1 + height1 > y2 && y1 < y2 + height2) || (y1 < y2 + height2 && y1 > y2);

        return false;
    }
}