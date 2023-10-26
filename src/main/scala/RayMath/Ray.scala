package RayMath

/**
 * A 3D vector.
 *
 * @param origin The origin of the vector.
 * @param direction The direction of the vector.
 */

class Ray(var origin: Vector3D, var direction: Vector3D):
    if direction.length != 1 then
        this.direction = direction.normalize