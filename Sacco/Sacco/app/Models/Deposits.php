<?php
namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class Deposits extends Model
{
    protected $table = 'deposits'; 
    public $timestamps = false;

    protected $fillable = ['member_id', 'receipt_number', 'amount', 'date'];

    protected $primaryKey = 'receipt_no';
    public $incrementing = false;
}

