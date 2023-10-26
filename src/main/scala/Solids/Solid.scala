package Solids

import RayMath.{Ray, RotationMatrix3D, Vector3D}
import Rendering.Material

/**
 * Superclass for all solids in the scene
 * @param position The position of the solid
 * @param material The material of the solid
 */

trait Solid(var position: Vector3D, val material: Material):

  var rotationMatrix: RotationMatrix3D = RotationMatrix3D(0,0,0)

  /**
   * Calculates the intersection of a ray with the solid
   * @param ray The ray to calculate the intersection with
   * @return The intersection point if it exists
   */
  def calculateIntersection(ray: Ray): Option[Vector3D]

  /**
   * Calculates the normal vector at a point on the solid
   * Assumes that the point is on the surface of the solid
   * @param point The point to calculate the normal vector at
   * @return The normal vector
   */
  def normalAt(point: Vector3D): Vector3D

  /**
   * Calculates the color of the solid at a point
   * Assumes that the point is on the surface of the solid
   * @param point The point to calculate the color at
   * @return The color at the point
   */
  def getTextureColor(point: Vector3D): Vector3D
