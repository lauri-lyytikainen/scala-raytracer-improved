package Solids

import RayMath.{Ray, RayHit, RotationMatrix3D, Vector3D}
import Rendering.Material
import scalafx.scene.paint.Color

/**
 * Superclass for all solids in the scene
 */

trait Solid(var position: Vector3D, val material: Material):

  var rotationMatrix: RotationMatrix3D = RotationMatrix3D(0,0,0)
  
  def calculateIntersection(ray: Ray): Option[Vector3D]
  def normalAt(point: Vector3D): Vector3D
  def getTextureColor(point: Vector3D): Vector3D
